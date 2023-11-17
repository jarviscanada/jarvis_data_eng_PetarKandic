public class CurrencyService
{

    private CurrencyRepo repo;

    public CurrencyService(CurrencyRepo repo) {
        this.repo = repo;
    }

    public boolean isValidCode(String code) {
        return repo.getCurrency(code).isPresent();
    }

}
