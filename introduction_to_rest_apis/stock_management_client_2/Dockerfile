FROM denoland/deno:1.30.3

EXPOSE 7073

COPY deno.json .
COPY import_map.json .

CMD ["run", "--allow-net", "src/main.ts"]
