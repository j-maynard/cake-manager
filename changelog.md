# Change log

1. Move existing project in to a separate directory

Start clean.  This isn't a big project and will make it easy for us to work quickly.  Start by moving the existing
project in to a new directory ('old') in this instance.  Then we create an empty spring project using a Spring starter.

2. Bring in the original CakeEntity, setup JPA and make '/cakes' work

The CakesEntity object represents our domain model and the setup of the underlying data store.  So we bring this in as
it was originally.  We then setup JPA over the top.  To prove this works we also create the CakeController class and
implement a method for retriving the list of cakes.

3. Create Dockerfile and setup CI/CD using GitHub actions.

The DockerFile is used to help us with development by giving a consistent environment in which to test out our Java
Application.  We can also use the Dockerfile in our CI/CD pipeline to deploy the resulting container to GitHub Packages.

4. Load cakes from GitHubGist as per the original application on startup

For the purposes of this application there is clearly a need to populate the application with data when it first starts
up.  So we create a ``@PostConstruct` component to load the data from GitHub Gist and persist the data.  The data has
duplicates on a unique field which I have taken to mean we need to handle this gracefully.

5. Implement remaining REST API end points

Create Cake (post /cakes), Delete cake (delete /cake/id), Get cake by id and title.

7. Implement basic frontend

8. Implement validation errors on the frontend
