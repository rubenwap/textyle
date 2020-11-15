package dev.rubensanchez

import java.io.File

object Search {

  def main(args: Array[String]): Unit = {
    val directory: File = new File(args(0))
    val files = if (directory.isDirectory) {
      directory.listFiles.filter(f => f.isFile && f.getName.endsWith(".txt")).toList
    } else {
      println(s"""Route $directory is not a valid directory. Please try again""")
      sys.exit(1)
    }

    println(s"""${files.length} files in the ${args(0)} directory""")

    val index = Match.readInMemory(files)
    println(index)

    while (true) {
      val query = scala.io.StdIn.readLine(">")
      if (query.toLowerCase() == ":q") {
        println("Bye!")
        sys.exit(0)
      } else {
        val tokens = query.split(" ").toList
        val rawMatches = Match.performMatching(tokens, index)
        val inContextMatches = Match.recomposeMatches(tokens, rawMatches)
        val scoredMatches = Match.calculateScore(inContextMatches, tokens)
        if (scoredMatches.isEmpty) {
          println("No results found")
        } else {
          scoredMatches.take(5).foreach(i => {
            println(s"""${i._2}%: ${i._1}""")
          })
        }
      }
    }


  }


}
