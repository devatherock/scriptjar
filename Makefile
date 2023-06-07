clean:
	./gradlew clean
test:
	./gradlew spotlessApply test -x compileGroovy -Dtest.logs=true $(additional_gradle_args)