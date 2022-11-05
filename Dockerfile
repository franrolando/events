FROM franff96/generic_database as img
ENTRYPOINT ["java","-jar","/transaction-logger-1.0.0.jar"]