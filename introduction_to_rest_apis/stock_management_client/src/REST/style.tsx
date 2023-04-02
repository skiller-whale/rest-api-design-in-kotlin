import React, { type FC, type PropsWithChildren } from "react";
import { A, H1 } from "../style.tsx";

export const Page: FC<PropsWithChildren> = ({ children }) => (
  <html>
    <head>
      <title>Warehouse Stock Management Client</title>
      <meta charSet="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <script src="https://cdn.tailwindcss.com?plugins=forms"></script>
    </head>
    <body className="max-w-5xl mx-auto flex flex-col gap-6 p-6 bg-orange-50">
      <div className="flex justify-between items-baseline">
        <H1>RESTful Warehouse Stock Management Client</H1>
        <div className="flex gap-3">
          <A href="/rest/warehouses">Warehouses</A>
          <A href="/rest/stock-lines">Stock Lines</A>
        </div>
      </div>
      {children}
    </body>
  </html>
);
