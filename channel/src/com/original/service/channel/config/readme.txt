1. run service 
	C:\mongodb\bin>mongod.exe
	if failed, then repari
	C:\mongodb\bin>mongod.exe --repair
	
2. run client 
	c:\mongodb\bin>mongo.exe
3. show db
	> show dbs
4. use db
	> use song
5. show collections
	> show collections
7. drop a collection ab.{dbname}.drop()
	> db.profile.drop()
8.import mongoimport --db {dbname} --collection {collectionname} --jsonArray c:\{finename}.json
	c:\mongodb\bin>mongoimport.exe --db song --collection profile  --jsonArray c:\profile.json
===================

User --help and \Tab to tip your self!

1. Run Services
	c:\mongodb\bin>mongod.exe
	
	mongod.exe --help for help and startup options
2. Connect MongoDB
	c:\mongodb\bin>mongo
	
3. Create a database or table	
	show dbs – List all databases.
	use db_name – Switches to db_name.
	show collections – List all tables in the current selected database.
	
4. Insert A Record
	> db.users.insert({username:"mkyong",password:"123456"})


5. Update A Record
	> db.users.update({username:"songxueyong"},{$set:{password:"beibei"}})

>6. Find Records
	> db.users.find();
	> db.users.find({username:"songxueyong"});
	
7. Delete Record
		> db.users.remove({username:"google"})
		
8. Indexing
	> db.users.getIndexes()
	> db.users.ensureIndex({username:1})

8.3 To drop an index, uses db.tablename.dropIndex(column). In below example, the index on column “username” is deleted or dropped.	
	> db.users.dropIndex({username:1})
	
	
	
9 Usage import json file
	mongoimport --db users --collection contacts --type csv --file /opt/backups/contacts.csv
	mongoimport --collection contacts --file contacts.json --journal
	mongoimport --db sales --collection contacts --stopOnError --dbpath /srv/mongodb/
	mongoimport --host mongodb1.example.net --port 37017 --username user --password pass --collection contacts --db marketing

8.4 To create an unique index, uses db.tablename.ensureIndex({column},{unique:true}). 
	db.users.ensureIndex({username:1},{unique:true});

10.1 help – All available commands.	
	> help
		db.help()                    help on db methods
		db.mycoll.help()             help on collection methods
		rs.help()                    help on replica set methods
		help admin                   administrative help
		help connect                 connecting to a db help
		help keys                    key shortcuts  
		db.help() – Shows help on db.
	> db.users.help()
		db.users.find().help() - show DBCursor help
	> db.users.find().help()

http://localhost:28017/

 1. mongod –help
 
 2. Install as Windows Service	
	#> mongod --dbpath "c:\mymongodb" --logpath "c:\mymongodb\logs.txt" --install --serviceName "MongoDB"
3. Uninstall It
	To uninstall above installed MongoDB service, issue “mongod --remove“, along with the installed service name.
	#> mongod --remove --serviceName "MongoDB"

MongoDB need repair if server is crashed
	
	 Old Lock File: \Data\Db\Mongod.Lock, Probably Means Unclean Shutdown
	1. Find the “\data\db\mongod.lock” file, and delete it.
	2.mongod --repair