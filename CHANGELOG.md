# Change Log
## 0.2.2 - 2019-11-01
### Changes
- Make `add-or-update-by-id-event` public

## 0.2.1-1 - 2018-05-04
### Added
- Error handler to each of the existing ajax request sugar functions

### Fixed
- Error-handler accidentally firing

## 0.2.1 - 2018-05-03
### Added
- Allow on-error and error-handler params to ajax requests
- Allow handler param to ajax requests (alias for on-success)
### Changed
- Handle fn or dispatcher for ajax requests

## 0.2.0 - 2018-?-?
### Added
- reg-ajax-delete-event
- Ability to alter on-success keys for ajax events, as well as manipulate DB with a DB fn
- sort-fn arg to reg-basic-sub (3-arity)

### Changed
- Bump all dependencies (breaking changes to cljs-ajax)

### Fixed
- Nested get for subscriptions not working

## 0.1.4 - 2016-07-07
### Added
- reg-sub-by-id

## 0.1.3 - 2016-11-03
### Fixed
- Data not returning following error being thrown

## 0.1.2 - 2016-11-01
### Added
- reg-update-by-id-event
- reg-add-or-update-by-id-event
- reg-ajax-post-event
- reg-ajax-put-event

### Changed
- Allow for reframe-utils/http effect to take a variable number of parameters for any custom parameters passed to the ajax.core requests
- Bump clojurescript to 1.9.293

## 0.1.1 - 2016-09-21
### Added
- Prototype reg-ajax-get-event

### Changed
- Bump clojurescript to 1.9.229
- Add docstrings

## 0.1.0 - 2016-09-06
### Added
- reg-basic-sub
- reg-set-event
- reg-add-event
- reg-update-event
- reg-remove-event
- multi-generation

All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).