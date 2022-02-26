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
        logger.info(f'*** {self.game.url()} [{line.room}] {line.username}: {line.text.encode("utf-8")}')
        if line.text[0] == self.command_prefix:
            self.command(line, game, line.text[1:].lower())

    def command(self, line, game, cmd):
        if cmd == "commands" or cmd == "help":
            self.send_reply(line, "Supported commands: !wait (wait a minute for my first move), !name, !howto, !eval, !queue, !time")
        elif cmd == "wait" and game.is_abortable():
            game.ping(60, 120, 120)
            self.send_reply(line, "Waiting 60 seconds...")
        elif cmd == "name":
            name = game.me.name
            self.send_reply(line, f"{name} running {self.engine.name()} (lichess-bot v{self.version})")
        elif cmd == "howto":
            self.send_reply(line, "How to run: Check out 'Lichess Bot API'")
        elif cmd == "eval" and line.room == "spectator":
            stats = self.engine.get_stats(for_chat=True)
            self.send_reply(line, ", ".join(stats))
        elif cmd == "eval":
            self.send_reply(line, "I will defeat you in 5 moves. just kidding it is a command only for spectators")
        elif cmd == "queue":
            if self.challengers:
                challengers = ", ".join([f"@{challenger.challenger_name}" for challenger in reversed(self.challengers)])
                self.send_reply(line, f"Challenge queue: {challengers}")
            else:
                self.send_reply(line, "No challenges queued.")
        elif cmd == "time":
            now = datetime.now()
            current_time = now.strftime("%H:%M:%S")
            self.send_reply(line, "current time is {}".format(current_time))
        elif cmd == "id":
            self.send_reply(line, "RaviharaV")

    def send_reply(self, line, reply):
        self.xhr.chat(self.game.id, line.room, reply)

    def send_message(self, room, message):
        if message:
            self.send_reply(ChatLine({"room": room}), message)


class ChatLine:
    def __init__(self, json):
        self.room = json.get("room")
        self.username = json.get("username")
        self.text = json.get("text")
