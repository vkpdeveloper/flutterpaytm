import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart';

class FlutterPaytm {
  String _mId;
  String _checksumURL;
  String _verificationURL;
  String _industry;
  String _website;
  bool _isTesting;

  static const MethodChannel _channel =
      const MethodChannel('flutterpaytm');

  void configPaytm({@required String mid, @required String checksumURL, @required String verificationURL, @required String industryType, @required String website, @required bool isTesting}) {
    _mId = mid;
    _checksumURL = checksumURL;
    _verificationURL = verificationURL;
    _industry = industryType;
    _website = website;
    _isTesting = isTesting;
  }


  Future<Map<dynamic, dynamic>> startPayment({@required String orderId, @required String customerId, @required String amount}) async {
    Response res2 = await post(_checksumURL, body: {
      "ORDER_ID": orderId,
      "CUST_ID": customerId,
      "INDUSTRY_TYPE_ID": _industry,
      "WEBSITE": _website,
      "MID": _mId,
      "CHECKSUM_URL": "${_checksumURL}?ORDER_ID=$orderId",
      "CHANNEL_ID": "WAP",
      "TXN_AMOUNT": amount,
    });
    String CHECKSUMHASH = json.decode(res2.body)['CHECKSUMHASH'];
    Map<dynamic, dynamic> response = await _channel.invokeMethod("startPayment", {
      "mid": _mId,
      "checksum": CHECKSUMHASH,
      "verificationURL": _verificationURL,
      "industry": _industry,
      "website": _website,
      "isTesting": _isTesting,
      "orderId": orderId,
      "customerId": customerId,
      "amount": amount
    });
    if(response != null){
      return response;
    }else{
      response['error'] = true;
      response['result'] = "Response is null";
      return response;
    }
  }

}
