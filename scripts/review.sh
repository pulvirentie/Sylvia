#!/usr/bin/env bash
isPR=${TRAVIS_PULL_REQUEST:-"false"}

if [[ "$isPR" != "false" ]]
then
    echo "Performing Automatic Review on Pull Request"
    bundle install
    ./gradlew ktlintCheck
    mkdir -p build/reports/ktlint
    python scripts/mergektlint.py
    bundle exec danger
else
    echo "Skipping Automatic Review because this is not a Pull Request"
fi
