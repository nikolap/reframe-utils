(ns reframe-utils.core
  (:require
    [re-frame.core :refer [reg-sub
                           reg-event-db]]))

(defn- collify
  [item]
  (if (coll? item)
    item
    [item]))

(defn- kw-prefix
  [kw prefix]
  (let [split-kw (clojure.string/split (subs (str kw) 1) #"/")]
    (keyword
      (if (= (count split-kw) 1)
        (str prefix (first split-kw))
        (let [[kw-ns kw-name] split-kw]
          (str kw-ns "/" prefix kw-name))))))

(defn- remove-when
  [coll item]
  (remove #(= % item) coll))

;; SUBSCRIPTION UTILITIES

(defn reg-basic-sub
  ([name k]
   (reg-sub
     name
     (fn [db _]
       (k db))))
  ([k]
   (reg-basic-sub k k)))

;; EVENT/HANDLER UTILITIES

(defn reg-set-event
  ([event-kw kw]
   (let [kw (collify kw)]
     (reg-event-db
       event-kw
       (fn [db [_ v]]
         (assoc-in db kw v)))))
  ([k]
   (assert (not (coll? k)) "1-arity reg-set-event must pass a keyword as the k, not a collection")
   (reg-set-event (kw-prefix k "set-") k)))

(defn reg-add-event
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ item]]
        (update-in db kw conj item)))))

(defn reg-update-event
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ old new]]
        (update-in db kw #(replace {old new} %))))))

(defn reg-remove-event
  [event-kw kw]
  (let [kw (collify kw)]
    (reg-event-db
      event-kw
      (fn [db [_ item]]
        (update-in db kw remove-when item)))))

;; GENERAL UTILITIES

(defn multi-generation
  [gen-fn & params]
  (doseq [p params]
    (if (coll? p)
      (apply gen-fn p)
      (gen-fn p))))