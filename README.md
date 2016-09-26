# reframe-utils

[![Clojars Project](http://clojars.org/reframe-utils/latest-version.svg)](https://clojars.org/reframe-utils)

A collection of commonly used utility functions/helper functions for re-frame.

## Usage

If you've been using the magical [reagent](https://github.com/reagent-project/reagent)  and [re-frame](https://github.com/Day8/re-frame) libraries, and are anything like me, you will have implemented a bunch of helper functions that manage common subscription and event/handler registration events that you create. After copy and pasting these like a plebe from project to project it seemed like about time to put together a library that has commonly used ones. The purpose of this library is to add syntactic sugar to help you reduce the lines of code you need to write when using re-frame.

If you have any additional helper functions you wish to submit or suggest, please make a pull request or add an issue!

To begin using the library ensure you have the latest reframe-utils included in your leiningen or boot dependencies and add the following to any namespaces you wish to use the utilities with.

It's worth noting that all of the utilities in this library can handle namespaced keywords, e.g. :library/books, intelligently.

Caveat: we assume that your re-frame db is going to be managed as map. I mean, I doubt anyone has the audacity to try something crazy and not do that, but who knows!

`(require [reframe-utils.core :as rf-utils])`

###Subscription utilities

####`reg-basic-sub`####
Used to register a basic get query from the database

```clojure
(reg-basic-sub :common/active-page)
;; Equivalent to
(reg-basic-sub :common/get-active-page :common/active-page)
;; Equivalent to
(reg-sub
	:common/get-active-page
	(fn [db _]
		(:common/active-page db)))
```

###Event/handler utilities

#### `reg-set-event` ####
Used to register a basic associative set to a keyworded value in the  database

```clojure
(reg-set-event :active-page)
;; Equivalent to
(reg-set-event :set-active-page :active-page)
;; Equivalent to
(reg-event-db
	:set-active-page
	(fn [db [_ page]]
		(assoc db :active-page page)))
```

Note for the following event/handler utilities you can do crazy stuff like this which lets you update/change values of nested keywords
```clojure
(reg-set-event :set-deep-deep-value [:deep :super-deep :super-duper-deep])
;; Equivalent to
(reg-event-db
	:set-deep-deep-value
	(fn [db [_ page]]
		(assoc-in db [:deep :super-deep :super-duper-deep] page)))
```

Eligible for crazy nested business
- `reg-set-event`
- `reg-add-event`
- `reg-update-event`
- `reg-remove-event`

#### `reg-add-event` ####
Used to register a basic conj update to a keyworded value in the database

```clojure
(reg-add-event :cases/add-case :cases/case)
;; Equivalent to
(reg-event-db
	:cases/add-case
	(fn [db [_ case]]
		(update db :cases/case conj case)))
```

#### `reg-update-event` ####
Used to register a basic replace update to a keyworded value in the database. Requires a given old and new value.

```clojure
(reg-update-event :cases/update-case :cases/case)
;; Equivalent to
(reg-event-db
	:cases/update-case
	(fn [db [_ old-case new-case]]
		(update db :cases/case #(replace {old new} %))))
```

#### `reg-remove-event` ####
Used to register a basic remove update to a keyworded value in the database. Removes the exact value that is passed through.

```clojure
(reg-remove-event :cases/delete-case :cases/case)
;; Equivalent to
(reg-event-db
	:cases/delete-case
	(fn [db [_ case]]
		(update db :cases/case 
			(fn [cases] 
				(remove #(= % case) cases)))))
```

#### `reg-update-by-id-event` ####
EXPERIMENTAL. View source for usage details.

#### `reg-add-or-update-by-id-event` ####
EXPERIMENTAL. View source for usage details.

###General utilities

#### `multi-generation` ####
Used to generate multiple events or subscriptions at one go
```clojure
(multi-generation reg-basic-sub
			      :active-page
			      [:active-cow :cow])
;; will generate two subscriptions, active-page and active-cow
```

###AJAX utilities

#### `reg-ajax-get-event` ####
```clojure
(reg-ajax-get-event "/api/request-call" :data)
;; Equivalent to
(reg-ajax-get-event "/api/request-call" :get-data :data)

;; You would then dispatch the event as follows
(dispatch [:get-data])

;; If there are variable uri params to pass through you can register an event as follows
(reg-ajax-get-event "/api/items/%s" :item)
;; And then you would dispatch it like so
(dispatch [:get-item 1]) ;; => call GET on "/api/items/1" and assoc-in the response to :item
```

#### `reg-ajax-post-event` ####
EXPERIMENTAL. View source for usage details.

#### `reg-ajax-put-event` ####
EXPERIMENTAL. View source for usage details.

## License

Copyright © 2016 Nikola Peric

Distributed under the MIT License
