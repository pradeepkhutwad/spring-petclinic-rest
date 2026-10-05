# Prompt Log

- Add edge-case validations for visits: reject past dates, duplicate visits in the same slot, and visits for non-existent pets.
- Highlight the visit controller and use `/tests` to generate parameterized JUnit 5 tests with `@ParameterizedTest` and `@CsvSource`; verify HTTP 400 for past dates using MockMvc, AssertJ, and Mockito fixtures.
- Export a short prompt-log Markdown file.
