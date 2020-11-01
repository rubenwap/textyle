package dev.rubensanchez

import java.io.File

import scala.io.Source

object Match {

  case class Match(word: String, index: Int, filename: String)

  def tokenizeText(text: String): Array[String] = {

    for (word <- text.trim().toLowerCase().split(" ") if word.length() > 0)
      yield word
        .replaceAll("""[\p{Punct}]|[\p{Space}]""", "")
  }

  def readInMemory(files: List[File]): List[Match] = {

    files.map(f => {
      val bufferedSource = Source.fromFile(f)
      val tokens = tokenizeText(bufferedSource.getLines().mkString)
      bufferedSource.close
      tokens.map(t => Match(t, tokens.indexOf(t), f.getName))
    }
    ).flatten(f => f)
  }

  def performMatching(tokens: List[String], index: List[Match]): List[Match] = {
    index.filter(item => tokens.contains(item.word)).sortBy(m => m.index)
  }

  def recomposeMatches(tokens: List[String], results: List[Match]): List[Match] = {
    if (tokens.length == 1) {
      results.take(5)
    } else {
      results.sliding(2).filter(m => m.head.index == (m(1).index - 1) && m.head.filename == m(1).filename).flatten(m => m).toList
    }
  }

  def calculateScore(filteredMatches: List[Match], tokens: List[String]): List[(String, Int)] = {
    val classifiedMatch = filteredMatches.distinct.groupBy(m => m.filename)
    classifiedMatch.keys.map(k => (k, classifiedMatch(k).length * 100 / tokens.length)).toList
  }

}
