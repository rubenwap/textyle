# Textyle
## Command line text search with Scala

Textyle is a command line app to search text quickly and efficiently within large directories of text files. 

Originally made as an exercise to learn about the methods and quirks of Scala to manipulate collections and strings, this can be used in real scenarios or improved upon. 

## What it does:

It indexes the tokenized text of your .txt files and gives it an index position. When you later input a search string in the console application, it will compare not only if your search words appear in the indexed tokens, but also if the index order is consecutive, so to speak, if your words are matching in the same order as you wrote them. It will return results for the first five files. I think than this way of searching scales a bit better than doing the full search at once. 

## Uses:

* AdoptJDK 1.8
* Scala 2.13

No external libraries for the application itself. Only for testing. 

## Run:

You can run it straight from the source with sbt. Run the main class with your text files folder as argument, such as 

```
runMain dev.rubensanchez.Search /Users/ruben/documents
```

But the recommended way is to compile a jar first. I use [sbt-assembly](https://github.com/sbt/sbt-assembly)

Once you have compiled it, you can run it like this:

```
java -jar textyle-assembly-0.1.jar /Users/ruben/documents
```

And the result will be faster and nicer than running it from sbt. 

## Exit:

Use `:q` to quit (case insensitive)






