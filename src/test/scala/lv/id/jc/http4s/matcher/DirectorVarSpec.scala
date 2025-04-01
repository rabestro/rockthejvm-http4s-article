package lv.id.jc.http4s.matcher

import lv.id.jc.http4s.model.Director
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DirectorVarSpec extends AnyWordSpec with Matchers {

  "DirectorVar extractor" should {

    "extract a director from a valid full name" in {
      DirectorVar.unapply("Zack Snyder") shouldBe Some(Director("Zack", "Snyder"))
    }

    "return None for a single name" in {
      DirectorVar.unapply("Madonna") shouldBe None
    }

    "return None for an empty string" in {
      DirectorVar.unapply("") shouldBe None
    }

    "extract a director while ignoring extra spaces" in {
      DirectorVar.unapply("  Zack    Snyder  ") shouldBe Some(Director("Zack", "Snyder"))
    }

    "return None for input with more than two words" in {
      DirectorVar.unapply("Christopher Nolan Test") shouldBe None
    }

    "return None for input containing special characters" in {
      DirectorVar.unapply("Quentin@Tarantino") shouldBe None
    }
  }
}