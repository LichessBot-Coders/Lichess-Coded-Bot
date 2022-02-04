import logging

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
            self.send_reply(line, "Supported commands: !wait(only works at start of the game), !name, !howto, !eval, !queue, !id, !github")
        elif cmd == "wait" and game.is_abortable():
            game.ping(60, 120)
            self.send_reply(line, "Waiting 60 seconds...")
        elif cmd == "name":
            name = game.me.name
            self.send_reply(line, "{} using c++ and java codes running {} (lichess-bot v{}) on heroku server.".format(name, self.engine.name(), self.version))
        elif cmd == "id":
            self.send_reply(line, "@RaviharaV")
        elif cmd == "howto":
            self.send_reply(line, "How to run your own bot: Check out 'Lichess Bot API'")
        elif cmd == "eval":
            stats = self.engine.get_stats()
            self.send_reply(line, ", ".join(stats))
        elif cmd == "eval":
            self.send_reply(line, "That's the evaluation of the position according to my engine! ")
        elif cmd == "queue":
            if self.challengers:
                challengers = ", ".join(["@" + challenger.challenger_name for challenger in reversed(self.challengers)])
                self.send_reply(line, "Challenge queue: {}".format(challengers))
            else:
                self.send_reply(line, "No challenges queued. Wait for my current game to finish then kindly challenge.")
        elif cmd == "github":
            self.send_reply(line, "https://github.com/RaviharaV-bot/Lichess-Coded-Bot")
                
    def send_reply(self, line, reply):
        self.xhr.chat(self.game.id, line.room, reply)


class ChatLine:
    def __init__(self, json):
        self.room = json.get("room")
        self.username = json.get("username")
        self.text = json.get("text")
