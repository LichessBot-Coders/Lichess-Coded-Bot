FROM debian:stable-slim
MAINTAINER OIVAS7572
RUN echo OIVAS7572
CMD echo OIVAS7572
COPY . .

#ADD /engine/ .
#RUN rm -r engine

# If you want to run any other commands use "RUN" before.

RUN apt-get update && apt-get install -y wget python3 python3-pip p7zip-full

RUN wget --no-check-certificate "https://gitlab.com/OIVAS7572/Goi5.1.bin/-/raw/main/Goi5.1.bin.7z" -O Goi5.1.bin.7z
RUN 7z e Goi5.1.bin.7z
RUN rm Goi5.1.bin.7z
RUN wget --no-check-certificate "https://gitlab.com/OIVAS7572/Cerebellum3merge.bin/-/raw/main/Cerebellum3Merge.bin.7z" -O Cerebellum3Merge.bin.7z
Run 7z e Cerebellum3Merge.bin.7z
RUN rm Cerebellum3Merge.bin.7z
RUN wget --no-check-certificate "https://abrok.eu/stockfish/builds/442c40b43de8ede1e424efa674c8d45322e3b43c/linux64modern/stockfish_22062008_x64_modern.zip" -O stockfish_22062008_x64_modern.zip
#RUN wget --no-check-certificate "https://data.stockfishchess.org/nn/nn-938525872610.nnue" -O nn-938525872610.nnue
RUN 7z e chess-engine.zip && rm chess-engine.zip && mv stockfish* chess-engine

COPY requirements.txt .
RUN python3 -m pip install --no-cache-dir -r requirements.txt

RUN chmod +x chess-engine
# Engine name is here ^^^^^^

CMD python3 run.py
