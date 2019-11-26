;; gorilla-repl.fileformat = 2

;; **
;;; # PinkGorilla WIDGETS
;;; 
;;; Widgets are Browser-Side components to interact easier with data.
;;; They are implemented with Reagent.
;; **

;; @@
(ns icy-plateau
  (:require [pinkgorilla.ui.widget :refer [widget!]]))
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","html","~:content",["span",["^ ","~:class","clj-nil"],"nil"],"~:value","nil"]
;; <=

;; @@
; Get some kind of error message if the widget is not found:
(widget! "widget-that-does-not-exist")
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","widget","~:content",["^ ","~:widget","widget-that-does-not-exist","~:initial-state",null]]
;; <=

;; @@
; Widgets can be parameter less (which does not make a lot of sense if they should interact with data,but it is possible

(widget! "widget.hello/world")

;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","widget","~:content",["^ ","~:widget","widget.hello/world","~:initial-state",null]]
;; <=

;; @@
; The second parameter is the initial state. This initial state is converted to an atom, so the widget can work on changing data.
(widget! "widget.hello/world" "Mr. Kim .com")
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","widget","~:content",["^ ","~:widget","widget.hello/world","~:initial-state","Mr. Kim .com"]]
;; <=

;; @@
; a binary clock. click on the clock to show/hide milliseconds
(widget! "widget.clock/binary-clock")
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","widget","~:content",["^ ","~:widget","widget.clock/binary-clock","~:initial-state",null]]
;; <=

;; @@
; TODO: MAKE THIS 2 WIDGETS INDEPENDENT OF EACH OTHER.
(widget! "widget.clock/binary-clock")
;; @@
;; ->
;;; 
;; <-
;; =>
;;; ["^ ","~:type","widget","~:content",["^ ","~:widget","widget.clock/binary-clock","~:initial-state",null]]
;; <=

;; @@

;; @@
;; ->
;;; 
;; <-
