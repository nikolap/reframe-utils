(ns reframe-utils.core
  (:require
    [re-frame.core :refer [reg-sub reg-event-db reg-event-fx dispatch]]
    [day8.re-frame.http-fx]
    [ajax.core :as ajax]))

(defn- collify
  "Given an item, returns the item if it is a coll,
   otherwise returns it wrapped in a vector"
  [item]
  (if (coll? item)
    item
    [item]))

(defn- kw-prefix
  "Takes a keyword and prefix. Appends the prefix to the keyword,
   taking into account namespacing"
  [kw prefix]
  (let [split-kw (clojure.string/split (subs (str kw) 1) #"/")]
    (keyword
      (if (= (count split-kw) 1)
        (str prefix (first split-kw))
        (let [[kw-ns kw-name] split-kw]
          (str kw-ns "/" prefix kw-name))))))

(defn- remove-when
  "Removes an item from a collection"
  [coll item]
  (remove #(= % item) coll))

;; SUBSCRIPTION UTILITIES

(defn reg-basic-sub
  "Registers a 'get' subscription from the re-frame db.

   (reg-basic-sub :sub-name :kw-to-get)
   (reg-basic-sub :sub-name [:kw-to-get1 :kw-to-get2])

   Using the one-arity version of this function will append 'get'
   as a prefix to the keyword for the name of the sub. For example
   (reg-basic-sub :best-ns/my-sub) registers a subscription named
   :best-ns/get-my-sub and returns :best-ns/my-sub from the db"
  ([name k]
   (reg-sub
     name
     (fn [db _]
       (k db))))
  ([k]
   (reg-basic-sub k k)))

;; EVENT/HANDLER UTILITIES

(defn reg-set-event
  "Registers a 'set' associative event.

   (reg-set-event :event-name :kw-to-set)
   (reg-set-event :event-name [:kw-to-set1 :kw-to-set2])

   Using the one-arity version of this function will append 'set'
   as a prefix to the keyword for the name of the revent. For example
   (reg-set-event :best-ns/my-kw) registers an event named
   :best-ns/set-my-kw that, when called, associates a value to
   :best-ns/my-kw db.

   As with other re-frame events you would call these through any form
   of the dispatch, e.g. (dispatch [:best-ns/set-my-kw [1 2 3 4]])"
  ([event-kw kw]
   (let [kw (collify kw)]
     (reg-event-db
       event-kw
       (fn [db [_ v]]
         (assoc-in db kw v)))))
  ([k]
   (assert (keyword? k) "1-arity reg-set-event must pass a keyword as the k, not a collection")
   (reg-set-event (kw-prefix k "set-") k)))

(defn reg-add-event
  "Registers an update event to the db that preforms a conj

   (reg-add-event :add-kw :kw)
   (reg-add-event :add-kw [:kw1 :kw2])

   You would call the event as follows, passing through one item to conj

   (dispatch [:add-kw my-item])

   Note: there is no one-arity version of this function."
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ item]]
        (update-in db kw conj item)))))

(defn reg-update-event
  "Registers an update event to the db that preforms a replace

   (reg-update-event :update-kw :kw)
   (reg-update-event :update-kw [:kw1 :kw2])

   You would call the event as follows, passing through two items,
   the old item to replace, and the new one to replace it with

   (dispatch [:update-kw old-item new-item])

   Note: there is no one-arity version of this function."
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ old new]]
        (update-in db kw #(replace {old new} %))))))

(defn reg-remove-event
  "Registers an update event to the db that performs a remove-when.

   (reg-remove-event :remove-kw :kw)
   (reg-remove-event :remove-kw [:kw1 :kw2])

   Remove-when is a helper function that calls remove on a collection
   and is true when any item in the collection equals the item to test
   against.

   You would call the event as follows, passing through one item,
   the one that you are removing from the collection.

   (dispatch [:remove-kw item])

   Note: there is no one-arity version of this function."
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ item]]
        (update-in db kw remove-when item)))))

(defn reg-ajax-get-event
  "WARNING: PROTOTYPE, MAY CHANGE

   Registers a reg-set-event event and an effectful get handler
   that performs an AJAX get request and, on success, dispatches
   the set event. Assumes set event has already been registered
   and calls it. If no get- and set- event keywords passed, appends
   get- and set- to the keyword."
  ([uri get-event-kw set-event-kw kw]
   (reg-event-fx
     get-event-kw
     (fn [{:keys [db]} _]
       {:db         db
        :http-xhrio {:method          :get
                     :uri             uri
                     :response-format :detect
                     :on-success      [set-event-kw]}})))
  ([uri kw]
   (reg-ajax-get-event uri (kw-prefix kw "get-") (kw-prefix kw "set-") kw)))

;; GENERAL UTILITIES

(defn multi-generation
  "Applies a generation function to each parameter passed through,
   up to a variable amount.

   (multi-generation reg-basic-sub :cow :wolf :dog :cat)
   ; => registers four subscriptions, :get-cow, :get-wolf, :get-dog :get-cat"
  [gen-fn & params]
  (doseq [p params]
    (if (coll? p)
      (apply gen-fn p)
      (gen-fn p))))