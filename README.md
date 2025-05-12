Run Information

Smoke test pack - mvn "-Dtest=smoke.HealthCheckTest "test

Regression test pack -mvn "-Dtest=regression.** "test

Run Allure report -

allure generate target/allure-results --clean -o target/allure-report
allure open target/allure-report