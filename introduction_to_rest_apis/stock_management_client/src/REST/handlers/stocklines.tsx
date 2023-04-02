import React from "react";
import { type Handler } from "./types.ts";
import { htmlResponse } from "../response.tsx";
import { H2, List } from "../../style.tsx";

// /stock-lines
const stockLines: Handler = ({ stockLines }) => htmlResponse(
  <>
    <H2>Stock Lines</H2>
    <List>
      {stockLines.map((stockLine) => (
        <li>{stockLine.attributes.name}</li>
      ))}
    </List>
  </>
);

export default stockLines;
