
find . -type f -not -path '*/\.*' -not -path '*/\^archetype\-catalog\.xml*' -not -path '*/\^maven\-metadata\-local*\.xml' -not -path '*/\^maven\-metadata\-deployment*\.xml' | sed "s|^\./||" | xargs -I '{}' curl -u "admin:password" -X PUT -v -T {} http://10.0.3.23:49167/repository/maven-releases/{} ;
