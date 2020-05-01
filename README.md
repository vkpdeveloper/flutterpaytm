# FlutterPaytm

A new Flutter plugin for Paytm Payment Gateway support with Flutter.

## How to Install

```dart
    flutterpaytm: ^1.0.0
```

## How to Use

### Configuration
```dart
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
```

### Start a Payment
```dart
Future<void> doPayment() async {

    Map<dynamic, dynamic> res;
    String orderID = "YOUR ORDER ID HERE";
    String customerID = "YOUR CUSTOMER ID HERE";
    String amount = "AMOUNT HERE";
    try {
      res = await paytm.startPayment(orderId: orderID, customerId: customerID, amount: amount);
    } on PlatformException {
      // do something
    }

    if (!mounted) return;

    setState(() {
      response = res;
    });
  }
```

## Contact
Email: vaibhavpathak9984@gmail.com
Github: @vkpdeveloper
Telegram: @vkpdeveloper