import logging
from datetime import datetime 

logger = logging.getLogger(__name__)


class Conversation:
    def __init__(self, game, engine, xhr, version, challenge_queue):
        self.game = game
        self.engine = engine
        self.xhr = xhr
        self.version = version
        self.challengers = challenge_queue

    command_prefix = "!"

    def react(self, line, game):
        logger.info("*** {} [{}] {}: {}".format(self.game.url(), line.room, line.username, line.text.encode("utf-8")))
        if (line.text[0] == self.command_prefix):
            self.command(line, game, line.text[1:].lower())

    def command(self, line, game, cmd):
        if cmd == "commands" or cmd == "help":
            self.send_reply(line, "Supported commands: !wait, !name, !howto, !eval, !queue, !time")
        elif cmd == "wait" and game.is_abortable():
            game.ping(60, 120)
            self.send_reply(line, "Waiting 60 seconds...")
        elif cmd == "name":
            name = game.me.name
            self.send_reply(line, "{} using c++ and java codes running {} (lichess-bot v{}) on heroku server.".format(name, self.engine.name(), self.version))
        elif cmd == "id":
            self.send_reply(line, "RaviharaV")
        elif cmd == "howto":
            self.send_reply(line, "How to run your own bot: Check out 'Lichess Bot API' or go to https://github.com/LichessBot-Coders/Lichess-Coded-Bot")
        elif cmd == "eval" and line.room == "spectator":
            stats = self.engine.get_stats(for_chat=True)
            self.send_reply(line, ", ".join(stats))
        elif cmd == "eval":
            self.send_reply(line, "I will defeat you in 5 moves. just kidding it is a command only for spectators.")        
        elif cmd == "queue":
            if self.challengers:
                challengers = ", ".join([f"@{challenger.challenger_name}" for challenger in reversed(self.challengers)])
                self.send_reply(line, f"Challenge queue: {challengers}")
            else:
                self.send_reply(line, "No challenges queued.")e.")
        elif cmd == "time":
            now = datetime.now()
            current_time = now.strftime("%H:%M:%S")
            self.send_reply(line, "current time is {}".format(current_time))

    def send_reply(self, line, reply):
        self.xhr.chat(self.game.id, line.room, reply)


class ChatLine:
    def __init__(self, json):
        self.room = json.get("room")
        self.username = json.get("username")
        self.text = json.get("text")
