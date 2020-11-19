package dev.rubensanchez

import java.io.File

import scala.io.Source

object Match {

  case class Match(word: String, index: Int, filename: String)

  def tokenizeText(text: String): Array[String] = {

    for (word <- text.trim()
      .replaceAll("""[\p{Punct}]""", "")
      .replaceAll("""[\p{Space}]""", " ")
      .split(" ")
         if word.length() > 0)
      yield word.toLowerCase()

  }

  def readInMemory(files: List[File]): List[Match] = {

    files.map(f => {
      val bufferedSource = Source.fromFile(f)
      val tokens = tokenizeText(bufferedSource.getLines().mkString)
      bufferedSource.close
      tokens.zipWithIndex.map(t => Match(t._1, t._2, f.getName))
    }
    ).flatten(f => f)
  }

  def performMatching(tokens: List[String], index: List[Match]): List[Match] = {
    tokens.map(t => index.filter(item => item.word == t).sortBy(m => m.index)).flatten(m => m)
  }

  def recomposeMatches(tokens: List[String], results: List[Match]): List[Match] = {
    if (tokens.length == 1) {
      results
        .take(5)
    } else {
      results
        .sortBy(i => i.index)
        .sliding(tokens.length)
        .filter(m => m.head.index == (m(1).index - 1) && m.head.filename == m(1).filename)
        .flatten(m => m)
        .distinct
        .toList
    }
  }

  def calculateScore(filteredMatches: List[Match], tokens: List[String]): List[(String, Int)] = {
    filteredMatches.groupBy(m => m.filename)
      .map(l => Map(l._1 -> l._2.sliding(tokens.length))) // file groups
      .map(f => { // for every file group
        val lolMatches = f.map(f => f._2) // list of lists of possibilities
        val singleMatch = Map(f.keys.head -> lolMatches
          .map(m => m.map(l => l)).flatten) // analysis of single list
        val resultSingle = singleMatch.map(e => {
          val filename = e._1
          val matches = e._2.map(e => {
            println(e.map(w => w.word))
            e.map(part => part.index).sorted.sliding(2)
              .filter(n => n.last - n.head == 1).size + 1
          })
          (filename, matches.toList.sorted.last * 100 / tokens.length) // return filename and score
        })
        resultSingle
      })
      .flatten
      .take(5)
      .toList
  }

}
