package com.example.two_eventchannels

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import java.text.SimpleDateFormat
import java.util.*
import java.lang.Runnable as Runnable1

class MainActivity : FlutterActivity() {
    private val eventChannel1 = "com.example.two_eventchannels/events1";
    private val eventChannel2 = "com.example.two_eventchannels/events2";

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel1).setStreamHandler(TimeHandler);
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, eventChannel2).setStreamHandler(CounterHandler);

    }
}

// 타이머 스트리밍
object TimeHandler : EventChannel.StreamHandler {
    // Handle event in main thread,
    private var handler = Handler(Looper.getMainLooper())

    // Declare our eventSink later it will be initialized
    private var eventSink: EventChannel.EventSink? = null

    @SuppressLint("SimpleDateFormat")
    override fun onListen(p0: Any?, sink: EventChannel.EventSink?) {
        eventSink = sink
        // every second send the time
        val r: Runnable1 = object : Runnable1 {
            override fun run() {
                handler.post {
                    val dateFormat = SimpleDateFormat("HH:mm:ss")
                    val time = dateFormat.format(Date())
                    eventSink?.success(time)
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(r, 1000)
    }

    override fun onCancel(p0: Any?) {
        eventSink = null
    }
}

// 카운터 스트림
object CounterHandler : EventChannel.StreamHandler {
    // Handle event in main thread.
    private val handler = Handler(Looper.getMainLooper())

    //  Declare our eventSink later it will be initialized
    private var eventSink: EventChannel.EventSink? = null

    @SuppressLint("SimpleDateFormat")
    override fun onListen(p0: Any?, sink: EventChannel.EventSink?) {
        eventSink = sink
        var count: Int = 0
        // every 5 second send the time
        val r: Runnable1 = object : Runnable1 {
            override fun run() {
                handler.post {
                    count++;
                    eventSink?.success(count)
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(r, 1000)
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }
}
