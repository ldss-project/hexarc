package test

/** Template test. */
class TemplateTest extends AbstractTest:
  private val positiveValue = 1

  describe("A value") {
    describe("when positive") {
      it("should be greater than 0") {
        positiveValue should be > 0
      }
    }
  }
