import React from "react";
import { renderToString } from "react_dom_server";
import { Status } from "http";
import type { ApiErrorMessage } from "./handlers/types.ts";
import { Page } from "./style.tsx";

export const htmlResponse = (html: JSX.Element, status: Status = Status.OK): Response =>
  new Response(renderToString(<Page>{html}</Page>), responseInit("text/html", status));

  export const errorResponse = (error?: string): Response =>
  htmlResponse(
    <>
      <h2 className="text-xl font-semibold">Oops</h2>
      <p>Something went wrong with the stock management client.</p>
      {error ? <p>{error}</p> : null}
    </>,
    Status.InternalServerError
  );

export const apiErrorResponse = (responseBody: ApiErrorMessage, status: Status): Response =>
  htmlResponse(
    <>
      <h2 className="text-xl font-semibold">Oops</h2>
      <p>Something went wrong with the stock management server:</p>
      <ul>
        {responseBody.errors.map((error) => (
          <li>{error}</li>
        ))}
      </ul>
    </>,
    status
  );

export const redirectResponse = (path: string): Response =>
  new Response(null, redirectResponseInit(path));

const responseInit = (contentType: string, status: Status): ResponseInit => {
  const headers = new Headers(headersInit(contentType));
  return { headers, status };
};

const redirectResponseInit = (url: string): ResponseInit => {
  const headers = new Headers();
  headers.append("location", url);
  return { headers, status: Status.Found };
};

const headersInit = (contentType: string): HeadersInit => ({
  "content-type": contentType,
  date: new Date().toUTCString(),
});
