# Rocket Launch

Good morning, everyone! Today is a very exciting day as we prepare to launch our rocket into space.

We’ve been working hard to make sure everything is in order, and we’re confident that we’re ready to go. The weather is looking great, and we’re all feeling optimistic about the launch.

We’ll be keeping you updated throughout the day, so stay tuned for more information.

Thank you for joining us on this journey, and let’s get ready to blast off!

## Getting Started

### Android Studio
Install [Android Studio](https://developer.android.com/studio) and open the project, run a gradle sync to get started.

You can start editing the page by modifying any route segment under the package `app/src/main/java/com/neofinancial/neo/android/interview/screens/`.

---

## Brief

The lead engineer at Mission Control has been tasked with building a new application to track upcoming rocket launches.

To start, they need to display a paginated list of upcoming missions fetched from the API.
This will allow the controllers to see vital details like launch date, mission name, and type of rocket for all upcoming flights.

Next, the lead engineer works on building a countdown clock that will show the time remaining until the next launch.
Data is pulled from the API to make sure the countdown is accurate.

Finally, just before the big day, the team discovers a bug - the 2D rocket visualization is not showing the launch on the screen!
The lead engineer digs into the code and realizes they need to properly initialize the rocket and tie it into the countdown clock. After the quick update, the visuals should show the rocket launch on the screen.

On launch day, the new Mission Control Center application helps keep everything on schedule. The controllers use the information and tools provided to successfully get the rocket into orbit.
The lead engineer takes pride knowing their code supported the team through a successful launch!

### Mission Objectives

1. Objective 1: Add pagination support to display upcoming missions from the API.

    - Retrieve upcoming missions data from the API.

2. Objective 2: Build a countdown clock that displays the time remaining until the next launch.

    - Countdown timer counts down the seconds until the next upcoming launch.
    - Calculate the remaining time until the next launch by subtracting the current time from the launch time.

3. Objective 3: Show the rocket launch when countdown reaches zero.

    - When the countdown reaches 0, show the rocket lifting off.

#### Bonus Objectives

1. Bonus 1: Show the countdown T-minus clock in a human readable format (eg. MM:DD:HH:SS).

2. Bonus 2: Show the countdown T-minus clock for a launch chosen from the launch manifest.

3. Bonus 3: Make it pretty! Add some styling to the countdown clock and rocket launch visualization.

## API Documentation

```
GET https://interview-api.neofinancial.dev/api/launches
```

Returns an array of all rocket launches.

#### Query Params

| Key            | Description                                                                      | Examples                                                                |
| -------------- | -------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| limit          | Sets how many rocket launches should be returned                                 | `limit=2`, `limit=5`, `limit=10`                                        |
| offset         | Sets how many rocket launches should be skipped from the start of the result set | `offset=5`, `offset=10`                                                 |
| sort           | Sorts the rocket launches by the given field.                                    | `sort=launchDate`, `sort=name`, `sort=rocketType`, `sort=organization`  |
| order          | Orders the rocket launches in ascending or descending order                      | `order=asc`, `order=desc`                                               |
| filterBy       | Filters the rocket launches by the given field.                                  | `filterBy=rocketType`, `filterBy=organization&value=Orbital%20Dynamics` |
| filterOperator | The operator to use when filtering rocket launches by the given field.           | `filterOperator=eq`, `filterOperator=gte`, `filterOperator=lte`         |
| filterValue    | The value to use when filtering rocket launches by the given field.              | `filterValue=2025`, `filterValue=Orbital%20Dynamics`                    |

#### Sample Requests

- `GET /api/launches`
- `GET /api/launches?limit=2`
- `GET /api/launches?limit=5&offset=5`
- `GET /api/launches?sort=launchDate&order=asc`
- `GET /api/launches?limit=10&sort=launchDate&order=desc`
- `GET /api/launches?filterBy=launchDate&filterOperator=gte&filterValue=2025-03-04T00:00:00Z`
- `GET /api/launches?filterBy=organization&filterOperator=eq&filterValue=Orbital%20Dynamics`
- `GET /api/launches?filterBy=rocketType&filterOperator=eq&filterValue=large`
- `GET /api/launches?limit=10&sort=launchDate&order=asc&filterBy=launchDate&filterOperator=gte&filterValue=2025-03-04T00:00:00Z`

#### Responses

HTTP 200

```json
{
  "meta": {
    "total": 30,
    "limit": 10
  },
  "result": [
    {
      "slug": "falcon-alpha",
      "name": "Falcon Alpha",
      "launchDate": "2024-01-15T14:30:00Z",
      "rocketType": "medium",
      "organization": "Orbital Dynamics"
    },
    ...
  ]
}
```

HTTP 400

```json
{
  "error": "Invalid query parameter"
}
```
