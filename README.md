# Currelator-9000
Command line currency convertor/calculator.

   It understands Russian rubles and US dollars yet. Operands should be written in a special way, for examle: '$123.3' - for dollars and '123.3р' for rubles('р' should be cyryllic). It can understand convertational commands as well - toDollars() and toRubles(), and they can nest each other, like: 'toDollars(234р + toRubles($2.1))'.
   Currelator-9000 stores its settings in settings.properties file, user can choose rounding mode (HALF_UP, HALF_DOWN, HALF_EVEN from java RoundingMode), number of digits after decimal point and exchange rate(ratio).
   To quit the program, simply write "quit" in its command line.
