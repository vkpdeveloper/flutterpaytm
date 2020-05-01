import 'dart:convert';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutterpaytm/flutterpaytm.dart';
import 'package:http/http.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  FlutterPaytm paytm = FlutterPaytm();
  Map<dynamic, dynamic> response;
  String checksumURL = "Your Checksum generator URL Here";
  String mid = "YOUR MID HERE";
  String verificationURL = "Verification Callback URL";
  String industryType = "Retail";
  String website = "WEBSTAGING";
  bool isTesting = true;

  @override
  void initState() {
    super.initState();
    paytm.configPaytm(mid: mid, checksumURL: checksumURL, verificationURL: verificationURL, industryType: industryType, website: website, isTesting: isTesting);
  }

  Future<void> doPayment() async {

    Map<dynamic, dynamic> res;
    String orderID = "YOUR ORDER ID HERE";
    String customerID = "YOUR CUSTOMER ID HERE";
    String amount = "AMOUNT HERE";
    try {
      res = await paytm.startPayment(orderId: orderID, customerId: customerID, amount: amount);
    } on PlatformException {
      res['error'] = true;
      res['result'] = "PLATFORM EXCEPTION";
    }

    if (!mounted) return;

    setState(() {
      response = res;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: response != null ? Text(response.toString()) : MaterialButton(
            onPressed: doPayment,
            color: Colors.blue,
            height: 40.0,
            minWidth: 180,
            child: Text("Pay Me", style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16.0, color: Colors.white),),
          )
        ),
      ),
    );
  }
}
