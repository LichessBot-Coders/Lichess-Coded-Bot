FROM debian:stable-slim
MAINTAINER RAVIHARAV
RUN echo RAVIHARAV
COPY . .
COPY requirements.txt .

# If you want to run any other commands use "RUN" before.

RUN apt update > aptud.log && apt install -y wget python3 python3-pip p7zip-full > apti.log
RUN python3 -m pip install --no-cache-dir -r requirements.txt > pip.log

RUN wget --no-check-certificate -nv "https://gitlab.com/OIVAS7572/Goi5.1.bin/-/raw/MEGA/Goi5.1.bin.7z" -O Goi5.1.bin.7z \
&& 7z e Goi5.1.bin.7z && rm Goi5.1.bin.7z
RUN wget --no-check-certificate "https://gitlab.com/OIVAS7572/Cerebellum3merge.bin/-/raw/master/Cerebellum3Merge.bin.7z" -O Cerebellum3Merge.bin.7z
Run 7z e Cerebellum3Merge.bin.7z && rm Cerebellum3Merge.bin.7z
RUN bash makefish.sh
RUN wget --no-check-certificate "https://data.stockfishchess.org/nn/nn-938525872610.nnue" -O nn-938525872610.nnue

COPY requirements.txt .
RUN python3 -m pip install --no-cache-dir -r requirements.txt

RUN chmod +x stockfish_x64_modern
# Engine name is here ^^^^^^

CMD python3 run.py
