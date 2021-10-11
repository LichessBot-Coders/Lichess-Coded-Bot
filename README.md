# MAINTAINERS 
- [Kingnandi](https://lichess.org/@/Kingnandi) and [ChessGreatPlayer](https://lichess.org/@/ChessGreatPlayer)

[![Python](https://github.com/LichessBot-Coders/Lichess-Coded-Bots/actions/workflows/Python.yml/badge.svg)](https://github.com/LichessBot-Coders/Lichess-Coded-Bots/actions/workflows/Python.yml)
[![Docker](https://github.com/LichessBot-Coders/Lichess-Coded-Bots/actions/workflows/Docker.yml/badge.svg)](https://github.com/LichessBot-Coders/Lichess-Coded-Bots/actions/workflows/Docker.yml)
# lichess-bot
- A bridge between [Lichess API](https://lichess.org/api#tag/Bot) and bots.
- This bot is made with Python and it is running using Docker container and is concentrated on heroku.

## How to Install on Heroku
- Import or [Fork](https://github.com/LichessBot-Coders/Lichess-Coded-Bots/fork) this repository to your Github.
- Open the `config.yml` file and insert your [API access token](https://lichess.org/account/oauth/token/create?scopes[]=bot:play&description=Lichess+Bot+Token) in to token option and commit changes over [here](/config.yml#L1).
- Install [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli#download-and-install) and [create a new app](https://dashboard.heroku.com/new-app) in Heroku. <br/>
**Do note that in certain operating systems Heroku CLI doesn't get added to path automatically. If that's the case you'll have to add heroku to your path manually.**
- Run this command in cmd or powershell `heroku stack:set container -a appname`, where `appname` is replaced with your Heroku app's name.
- In heroku, in the `Deploy` tab click on `Connect to GitHub` and then click on `search` and select your fork/import of this repository.
- Now scroll down and under `Manual deploy`, click on `deploy` with the `master` branch selected. <br/> <br/>
Note: You could also `Enable Automatic Deploys` with the `master` branch selected if you would like each commit you make to get automatically and easily deployed onto your bot. It is your choice whether you'd like to Enable or Disable Automatic Deploys.
- After deploying wait for about 5 minutes till the build finishes and then in the `Resources` tab in heroku turn `worker` dynos. If you do not see any option to enable any dynos, then you'll have to wait for about 5 minutes and then refresh your page on heroku.

**You're now connected to lichess and awaiting challenges! Your bot is up and ready!**

## Bot Information
Engine:
- [Stockfish 14 dev](https://abrok.eu/stockfish/builds/ad357e147a1b8481a04761d726ce1db14115a68f/linux64modern/stockfish_21082721_x64_modern.zip) with the default NNUE.

Opening Books: 
- [Goi5.1.bin](https://gitlab.com/OIVAS7572/Goi5.1.bin/-/raw/master/Goi5.1.bin.7z)
- [Perfect2021.bin](/Perfect2021.bin)

**If you would like to run bot locally on PC, read the [lichess-bot manual](https://github.com/ShailChoksi/lichess-bot#how-to-install).**

## How to change the engine used?

**Changing the engine to an engine of your preference is simple. Just follow the following steps:**

- Firstly, you have to remove the engine used. To do this you need to put `#` at the start of these [lines 16 to 18 in the dockerfile](/Dockerfile#L16-L18) (or you can delete those lines).

- Then you need to download the binary of the chess engine you want to used and in your GitHub repository, Click on `Add files` and the click `Upload files` and upload the binary of the chess engine you have downloaded.

Note: Make sure you download a linux binary that is supported by heroku (by default Stockfish is used, but the default engine name is `chess-engine`).

- Then change the name of engine in [6th line of config.yml](/config.yml#L6) and [23rd line of Dockerfile](/Dockerfile#L23) to your binary file's name.

#### How to use Stockfish dev

- You can reset link in [20th line in Dockerfile](/Dockerfile#L20) to the Stockfish dev binary link from [abrok.eu/stockfish](http://abrok.eu/stockfish)
(You can set this `http://abrok.eu/stockfish/latest/linux/stockfish_x64_modern.zip` link for latest Stockfish dev binary)
#### How to use Stockfish nnue of your own choice

- You can reset link in [21th line in Dockerfile](/Dockerfile#L21) to the Stockfish nnue binary link from [https://tests.stockfishchess.org/nns](https://tests.stockfishchess.org/nns).Also in config.yml you have to set the use NNue to true to use nnue after putting binary link . This will be done in config.yml in line 43.

**Note: You need to use `Linux x64 for modern computers` binary for Heroku.**

## Acknowledgements
Credits to [Kingnandi](https://lichess.org/@/Kingnandi) and [ChessGreatPlayer](https://lichess.org/@/ChessGreatPlayer). They helped to collaborate and make this repository it contains codes of them and it is the best lichess repository to run bot in lichess by heroku server and a modified version of ShailChoksi.
