import { Server } from "http";
import { port } from "./constants.ts";
import handler from "./router.tsx";
import { errorResponse } from "./response.tsx";

const onError = (error: unknown): Response => {
  console.error(error);
  return errorResponse();
};

const app = new Server({ port, handler, onError });
app.listenAndServe();
console.log(`Stock management client 1 listening on port ${port}`);
