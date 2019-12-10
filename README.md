# Fractal Banking API
- Through this Springboot Restful web services, several end-points of Fractal API have been accessed for manipulating with the data for end users. Below end-points have been developed for easy access of data:
- Connect to Fractal Api and get the token for future access of other end-points through /fractal/token end-point
- Display all banks through end-point /fractal/banks
- Display all transactions based on bankId and accountId through end-point /fractal/banking/{bankId}/accounts/{accountId}/transactions
- List balances through end-point /fractal/banking/{bankId}/accounts/{accountId}/balances
- For better documentation, Swagger-2 has  been implemented.
