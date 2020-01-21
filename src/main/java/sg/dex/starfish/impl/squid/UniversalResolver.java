package sg.dex.starfish.impl.squid;

import sg.dex.starfish.Resolver;
import sg.dex.starfish.exception.ResolverException;
import sg.dex.starfish.util.DID;

import java.io.IOException;

public class UniversalResolver implements Resolver {
    private DexResolver dexResolver;
    private SquidResolverImpl squidResolverImpl;
    private static final String DEX_METHOD = "dex";

    /**
     * Create UniversalResolver
     *
     * @param DexResolver dexResolver
     * @param SquidResolverImpl squidResolverImpl
     */
    private UniversalResolver(DexResolver dexResolver, SquidResolverImpl squidResolverImpl)  {
        this.dexResolver = dexResolver;
        this.squidResolverImpl = squidResolverImpl;
    }
    /**
     * Creates UniversalResolver
     *
     * @throws IOException
     * @return UniversalResolver The newly created UniversalResolver
     */
    public static UniversalResolver create() throws IOException {
        SquidService squidService = SquidService.create("application_test.properties");
        return new UniversalResolver(DexResolver.create(), new SquidResolverImpl(squidService));
    }

    @Override
    public String getDDOString(DID did) throws ResolverException {
        if(did.getMethod() == DEX_METHOD) {
            return this.dexResolver.getDDOString(did);
        } else {
            return this.squidResolverImpl.getDDOString(did);
        }
    }

    @Override
    public void registerDID(DID did, String ddo) throws ResolverException {
        if(did.getMethod() == DEX_METHOD) {
            this.dexResolver.registerDID(did,ddo);
        } else {
            this.squidResolverImpl.registerDID(did,ddo);
        }
    }
}