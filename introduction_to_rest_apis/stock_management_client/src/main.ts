import { Server } from "http";
import { port } from "./constants.ts";
import basicHandler from "./router.tsx";
import restHandler from "./REST/router.tsx";
import { errorResponse } from "./response.tsx";

const onError = (error: unknown): Response => {
  console.error(error);
  return errorResponse();
};

const handler = (request: Request): Response | Promise<Response> => {
  const url = new URL(request.url);
  return url.pathname.startsWith("/rest")
    ? restHandler(request)
    : basicHandler(request)
}

const app = new Server({ port, handler, onError });
app.listenAndServe();
console.log(`Stock management client 1 listening on port ${port}`);
