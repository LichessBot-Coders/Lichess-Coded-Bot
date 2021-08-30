**MAINTAINER [Kingnandi](https://lichess.org/@/Kingnandi) and [ChessGreatPlayer](https://lichess.org/@/ChessGreatPlayer)**


## How to Install on Heroku
- Open the `config.yml` file and insert your [API access token](https://lichess.org/account/oauth/token/create?scopes[]=bot:play&description=Lichess+Bot+Token) in to token option and commit changes over [here](/config.yml#L1).
- Install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) and [create a new app](https://dashboard.heroku.com/new-app) in Heroku. <br/>
**Do note that in certain operating systems Heroku CLI doesn't get added to path automatically. If that's the case you'll have to add heroku to your path manually.**
- Run this command in cmd or powershell `heroku stack:set container -a appname`, where `appname` is replaced with your Heroku app's name.
- In heroku, in the `Deploy` tab click on `Connect to GitHub` and then click on `search` and select your fork/import of this repository.
- Now scroll down and under `Manual deploy`, click on `deploy` with the `master` branch selected.
- After deploying wait for about 5 minutes till the build finishes and then in the `Resources` tab in heroku turn `worker` dynos. If you 


## Bot Information
Engine:
- [Stockfish 14 dev  ](https://abrok.eu/stockfish/builds/ad357e147a1b8481a04761d726ce1db14115a68f/linux64modern/stockfish_21082721_x64_modern.zip) with the default NNUE.


## Acknowledgements
Credits to [Kingnandi](https://lichess.org/@/Kingnandi) and [ChessGreatPlayer](https://lichess.org/@/ChessGreatPlayer). They helped to collaborate and make this repository it contains codes of them and it is the best lichess repository to run bot in lichess by heroku server and a modified version of ShailChoksi.
