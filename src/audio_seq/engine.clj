(ns audio-seq.engine
  (:import (javax.sound.sampled
                       AudioFormat
                       AudioFormat$Encoding
                       AudioFileFormat
                       AudioFileFormat$Type
                       AudioInputStream
                       AudioSystem
           DataLine$Info SourceDataLine)
        (java.nio ByteBuffer)
        (java.io File ByteArrayInputStream)))

(def af (AudioFormat. 44100 16 1 true true))

(def buffer-size 1024)

(def ^:dynamic *sr* 44100)
(def ^:dynamic *ksmps* 64)

; JAVASOUND CODE

(defn open-line [audio-format]
  (let [#^SourceDataLine line (AudioSystem/getSourceDataLine audio-format)]
    (doto line 
    (.open audio-format)
    (.start))))

(defn run-audio-block [a-block]
  (let [#^SourceDataLine line (open-line af)
        audio-block a-block]
    (let [cnt (/ (* *sr* 5.0) buffer-size)
        buffer (ByteBuffer/allocate buffer-size)
        write-buffer-size (/ buffer-size 2)
        frames (quot write-buffer-size *ksmps*)]
      (loop [c cnt] 
       (when (> c 0) 
         (loop [x 0]
           (when (< x frames)
             (let [buf ^doubles (a-block)]
               (loop [y 0]
                 (when (< y (alength buf))
                   (.putShort buffer (short (* Short/MAX_VALUE (aget buf y))))
                   (recur (unchecked-inc y)))) 
               (recur (unchecked-inc x)))))
         (.write line (.array buffer) 0 buffer-size)
         (.clear buffer)
      (recur (dec c) ))))
    (.close line)))