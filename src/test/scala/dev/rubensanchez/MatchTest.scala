package dev.rubensanchez

import java.io.File

import dev.rubensanchez.Match._
import org.scalatest.funsuite.AnyFunSuite


class MatchTest extends AnyFunSuite {

  val testString: Array[String] = tokenizeText(
    """
        First Gentleman, You do not meet a man but frowns: our bloods
	    No more obey the heavens than our courtiers
	    Still seem as does the king.
        """)

  val expectedTokenized: Array[String] = Array[String](
    "first",
    "gentleman",
    "you",
    "do",
    "not",
    "meet",
    "a",
    "man",
    "but",
    "frowns",
    "our",
    "bloods",
    "no",
    "more",
    "obey",
    "the",
    "heavens",
    "than",
    "our",
    "courtiers",
    "still",
    "seem",
    "as",
    "does",
    "the",
    "king",
  )

  val files: List[File] = new File(getClass.getResource("/testFiles").getPath).listFiles.filter(_.isFile).toList
  val inMemoryIndex: List[Match] = readInMemory(files)
  val userTokens: List[String] = List("admired", "no", "messenger")
  val matching: List[Match] = performMatching(userTokens, inMemoryIndex)
  val filteredMatches: List[Match] = recomposeMatches(userTokens, matching)
  val finalScore: List[(String, Int)] = List(("test.txt",100), ("test2.txt",66))

  test("Input string should be correctly tokenized") {
    assert(testString === expectedTokenized)
  }

  test("Reading in memory should create an in memory index of correct length") {
    assert(inMemoryIndex.length === 71)
    assert(inMemoryIndex.head.word === "mark")
    assert(inMemoryIndex(70).word === "people")
  }

  test("Providing a search string should return a list of the index terms matching our query") {
    assert(matching.size === 5)
  }

  test("Matches are returned only if the consecutive word is also a match") {
    println(filteredMatches)
    assert(filteredMatches.size === 5)
  }

  test("Matches are scored according to the amount of user tokens found in the filtered matches") {
    assert(calculateScore(filteredMatches, userTokens) == finalScore)
  }

}
