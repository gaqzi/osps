Owe money, pay money (O$P$)
===========================

This is intended to be a chat bot that helps you split bills. And on the
occasions when one persons pays the entire bill, remember who owes how much.

It's also a learning exercise for Java and Spring. Any suggestions to help make
the code better is much appreciated.

# How to use

This is how I imagine a chat session with the bot to work:

```
< gaqzi> osps: new bill 110++, split between: Björn, Claudio, Rahul
< osps> Ok. Total amount is 129.47. An even split between [B]jörn, [C]laudio, and [R]ahul is: 43.16
< osps> If you want to go dutch specify the amount and the person.
< gaqzi> 15/3
< osps> Ok, splitting 15++ over everyone
< gaqzi> 20, B
< osps> Ok, added 20++ to Björn
< gaqzi> 30, B, R
< osps> Ok, splitting 30++ between Björn and Rahul
< gaqzi> 35, C
< osps> Ok, added 35++ to Claudio
< gaqzi> 10, R
< osps> Ok, added 10++ to Rahul
< osps> The bill has been fully accounted for. Per person:
< osps> Björn: 47.07, Claudio: 47.08, and Rahul: 35.32 = 129.47
```

The dream thing would be to be able to send a photo of the receipt to the bot,
and have it figure out quantities and sums per line item followed by it then
starting to ask who had what. Not expecting that to be a thing, though.

**Note:** At the time of writing the above is just an idea, nothing that is
actually working :P

# License

> "THE BEER-WARE LICENSE" (Revision 42):
>
> As long as you retain this notice you can do whatever you want with this
> stuff. If we meet some day, and you think this stuff is worth it, you can buy
> me a beer in return. Björn Andersson
