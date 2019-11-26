(ns pinkgorilla.kernel.bundled-dependencies
 (:require 
  cljs.analyzer.api
  cljs.analyzer
  cljs.env
  cljs.repl
  klipse-clj.repl
  clojure.spec.alpha
  clojure.walk
  clojure.zip
  clojure.edn
  clojure.data
  clojure.datafy
  clojure.set
  klipse-clj.tools
  
  ;; libraries we want in the bundle:
  reagent.core
  pinkgorilla.ui.gorilla-renderable
  pinkgorilla.ui.rendererCLJS
  pinkgorilla.kernel.cljs-tools
  
  fortune.core
  awb99.shapes.core
  
  quil.core
  quil.middleware

  ))
  
