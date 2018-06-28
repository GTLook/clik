curl -s https://jsonplaceholder.typicode.com/posts/ | jq -r '.[] | "\(.id)\t\(.title)"'
