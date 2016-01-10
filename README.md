# USaaS

Url Shortener as a Service. Simple Scala Play app to handle redirections using short urls


## Database
The database is configured to use Postgres, but it will support most relational databases. Assuming you are using Postgres:

Create a database user:
```
$ createuser usaas
```

Then create the database:
```
$ createdb --owner usaas url_shortener_aas
```

Finally generate the schema:
```
$ psql -f ./schema.sql url_shortener_aas usaas
```


## Running

To run locally, this will start the app and run it on port 9000:
```
$ sbt run
```

To run the tests:
```
$ sbt test
```

To build:
```
$ sbt package
```

One the application is running, you can view the API docs by browsing to `http://localhost:9000/docs/ui`

## Examples

Contained here are some sample interactions with the service using the (excellent) [HTTPie](https://github.com/jkbrzt/httpie) tool.

To create a new short url. This will return the inserted body (including an ID):
```
$ echo '{"original_url": "https://www.facebook.com/"}' |  http put http://localhost:9000/short-url/hash
```

To then query for the metadata after creation:
```
$ http get http://localhost:9000/short-url/hash/1
```

You can interact with the redirections then by going to `http://localhost:9000/some_hash`
