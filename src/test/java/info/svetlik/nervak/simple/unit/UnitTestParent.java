package info.svetlik.nervak.simple.unit;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
		UnitTestParent.EmptyContextBase.class
})
class UnitTestParent {

	static class EmptyContextBase {}

}
