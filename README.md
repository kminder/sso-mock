# Hadoop SSO Mockup #
This is a mockup of a Hadoop SSO integration with Keystone

First build the whole thing with {{mvn install}} in the root.
Then in three separate terminal cd into the submodues and execute {{mvn jetty:run}}.

Then you can access http://localhost:3333/hadoop to simulate access to a Hadoop UI and you will be redirected to a SSO endpoint for login.
You could also access http://localhost:1111/ambari to simulate access to the Ambari UI and be redirected to a SSO endpoint.
Lastly you can access http://localhost:2222/eci to simulate access to an external application and be redirected to a SSO endpoint.
In every case once you login at the SSO endpoint you should be able to access the remaining endpoints without being needing to login.

Any username will authentiate but the password must "-password" appended to the username.

For testing purposes you can also "logout" by accessing http://localhost:2222/logout


