# My take in the DSL Language Programming Patterns

# Part 1, lexer

Well, it starts with a basic generic pattern, LL

As a good first example, we try to create a lexer for lists:
```
[a, b]
```

## Test, create a test and code to parse a list

# Part 2, parsing, LL(1)

Grammar can be described as:
```
list    : '[' elements ']';
elements: element (',' element)*;
element : NAME;
NAME    : ('a'..'z'|'A'-'Z')+;
```

Now we can extend our grammar to support nested lists

```
list    : '[' elements ']';
elements: element (',' element)*;
element : NAME | list;
NAME    : ('a'..'z'|'A'-'Z')+;
```

# Part 2.2, parsing, LL(k)
Now let's extend the grammar a little more, we want to accept assignations so the following is valid:

```[a,b=c,d,e]```

For this we can extend our grammar like this:
```
list    : '[' elements ']';
elements: element (',' element)*;
element : NAME '=' NAME | NAME | list;
NAME    : ('a'..'z'|'A'-'Z')+;
```

One option is to write the grammar like this:

```
list    : '[' elements ']';
elements: element (',' element)*;
element : NAME ('=' NAME)? | list;
NAME    : ('a'..'z'|'A'-'Z')+;
```

But this is a little weird and not clear