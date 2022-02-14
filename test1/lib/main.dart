import 'package:flutter/material.dart';

void main() {
  runApp(new myapp());
}

class myapp extends StatefulWidget {
  const myapp({Key? key}) : super(key: key);

  @override
  _myappState createState() => _myappState();
}

class _myappState extends State<myapp> {


  String title = '';

  @override
  void initState() {
    // TODO: implement initState
    title ='click here';
    super.initState();
  }

   method1()
  {
    setState(() {
      title = 'The text is changed';
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'stateful widget',
      home: Scaffold(
        body: Center(
          child: ElevatedButton(
            onPressed:() {
             method1();
            },
            child: Text(title),
          ),
        ),
      ),
    );
  }


}
