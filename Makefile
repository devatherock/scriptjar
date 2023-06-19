skip_pull=false
all=false

clean:
	./gradlew clean
docker-build:
	docker build -t devatherock/scriptjar:latest -f docker/Dockerfile .
test:
ifeq ($(all), true)
	yamllint -d relaxed .circleci --no-warnings
endif
	SKIP_PULL=$(skip_pull) ./gradlew spotlessApply test -x compileGroovy -Dtest.logs=true $(additional_gradle_args)