# reframe-utils

[![Clojars Project](http://clojars.org/reframe-utils/latest-version.svg)](https://clojars.org/reframe-utils)

A collection of commonly used utility functions/helper functions for re-frame.

## Usage

TODO: add more details here... In meantime, if interested, review reframe-utils.core

`(require [reframe-utils.core :as rf-utils])`

###Subscription utilities

- `reg-basic-sub`
```clojure
(reg-basic-sub :active-page)
;;"Equivalent to"
(reg-basic-sub :active-page :active-page)
;;"Equivalent to"
(reg-sub
	:active-page
	(fn [db _]
		(k db)))
```

###Event/handler utilities

- `reg-set-event` - equivalent to `(assoc db kw v)`
- `reg-add-event` - equivalent to `(update-in db ks conj v)`
- `reg-update-event`
- `reg-remove-event`

###General utilities

- `multi-generation` - used to generate multiple events or subscriptions at one go
```clojure
(multi-generation reg-basic-sub
			      :active-page
			      [:active-cow :cow])
;; will generate two subscriptions, active-page and active-cow
```

## License

Copyright © 2016 Nikola Peric

Distributed under the MIT License
