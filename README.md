# librarian

Really simple java logging library.
When i got really tired from configuring legacy self-written loggers that we had on of my jobs,
i had an idea, that log is just file and i wanted logger to be based on file and filenames.
And to be configured as simple as possible.

What it doesn't do

- Thread safeness
- Rolling files
- Java error levels

What it does

- Orientated to name and files
- Split exceptions into message and stacktrace and write them to different files
- Have some formation (a little) for error messages
