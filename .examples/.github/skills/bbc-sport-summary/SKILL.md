---
description: Provides summary of the top BBC sport news of the day
---

1. use tools to open browser and navigate to the BBC sport website at https://www.bbc.co.uk/sport
2. use tools to evaluate JavaScript in the browser to extract ONLY the top story headlines text (not the entire page HTML). Use the browser_evaluate tool with a function parameter like: `() => { return Array.from(document.querySelectorAll('h2, h3, [class*="headline"]')).slice(0, 10).map(h => h.textContent.trim()).filter(t => t.length > 0).slice(0, 5); }` to get just the headline text strings as an array
3. summarise the teams and sports people included in those top 5 headlines
4. use tools to close the browser


