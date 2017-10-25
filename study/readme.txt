
local ldap, default admin (Apache DS) :
uid=admin,ou=system
secret

LDAP import (xxx.ldif) file
-- BEGIN --
#!RESULT OK
#!CONNECTION ldap://localhost:10389
#!DATE 2012-10-30T14:36:21.294
dn: ou=people,o=gosigma
changetype: add
ou: people
objectclass: organizationalUnit
objectclass: top

#! RESULT OK
#! CONNECTION ldap://localhost:10389
#! DATE 2012-10-30T14:36:21.320
dn: cn=James Xi,ou=people,o=gosigma
changetype: add
mail: james.xi@allbluesolutions.com
userpassword: password
description: A pirate captain and Peter Pan's nemesis
objectclass: inetOrgPerson
objectclass: organizationalPerson
objectclass: person
objectclass: top
sn: Hook
cn: James Hook
uid: jxi
-- END --

spark inside, refer : https://zhuanlan.zhihu.com/p/25772054

