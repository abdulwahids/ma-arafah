package tajmi.frontends;

import java.util.List;
import java.util.Random;
import tajmi.instances.vectorial.Vector;
import tajmi.instances.vectorial.VectorDistanceFunc;
import tajmi.instances.vectorial.som.VectorInitFunc;
import tajmi.instances.vectorial.som.VectorStopFunc;
import tajmi.instances.vectorial.som.VectorUpdateFunc;
import tajmi.instances.som.NaiveFindBestMatchFunc;
import tajmi.instances.som.SimpleShowStatusFunc;
import tajmi.abstracts.som.FindBestMatchFunc;
import tajmi.abstracts.som.InitFunc;
import tajmi.abstracts.som.NeighborhoodFunc;
import tajmi.abstracts.som.ProjectionFunc;
import tajmi.abstracts.som.ShowStatusFunc;
import tajmi.abstracts.som.StopFunc;
import tajmi.abstracts.som.UpdateFunc;
import tajmi.instances.som.GeneralProjectionFunc;
import tajmi.som.Field;
import tajmi.som.SOM;
import tajmi.som.SOMParams;

/**
 * This is where one should set the parameters for the SOM before creating it. <br>
 * Be aware: very stateful
 * @author badi
 */
public class SOMMaker {

    SOMParams params;
    List data = null;
    int field_len, field_width;

    FindBestMatchFunc find_bmu_func;
    InitFunc init_func;
    NeighborhoodFunc neighborhood_func;
    ProjectionFunc projection_func;
//    StopFunc stop_func;
    UpdateFunc update_func;
//    ShowStatusFunc show_status_func;

    public SOMMaker() {


        params = new SOMParams();


        params.iterations = 100;
        params.learning_restraint = 0.1;
        params.random_gen = new Random(42);
        params.restraint_modifier = 0.01;

        field_len = 50;
        field_width = 50;


    }

    public SOMMaker randomSeed(long seed) {
        params.random_gen = new Random(seed);

        return this;
    }

    public SOMMaker field_size(int len, int width) {
        field_len = len;
        field_width = width;

        return this;
    }

    public void setFind_bmu_func(FindBestMatchFunc find_bmu_func) {
        this.find_bmu_func = find_bmu_func;
    }

    public void setInit_func(InitFunc init_func) {
        this.init_func = init_func;
    }

    public void setNeighborhood_func(NeighborhoodFunc neighborhood_func) {
        this.neighborhood_func = neighborhood_func;
    }

    public void setProjection_func(ProjectionFunc projection_func) {
        this.projection_func = projection_func;
    }

    public void setShow_status_func(ShowStatusFunc show_status_func) {
        params.show_status_func = show_status_func;
    }

    public void setStop_func(StopFunc stop_func) {
        params.stop_func = stop_func;
    }

    public void setUpdate_func(UpdateFunc update_func) {
        this.update_func = update_func;
    }

    private SOM makeSOM(List data) {
        assert data != null : "Cannot create a SOM on empty data";

        InitFunc initf = init_func.params(data, params.random_gen);

        Field field = new Field(field_len, field_width, initf);
        params.field = field;

        params.iterations = 0;

        return new SOM(data, params);
    }

    /**
     * Creates a SOM over vectorial data using defaults if various functions have not been set: <br> <br>
     * <code>
     * projection_func = GeneralProjectionFunc <br>
     * find_best_match = NaiveFindBestMatchFunc <br>
     * update_func = VectorUpdateFunc <br>
     * show_status_func = SimpleShowStatusFunc <br> <br>
     * iterations = 100 <br>
     * learning_restraint = 0.1 <br>
     * random_gen = Random(42) <br>
     * restraint_modifier = 0.01 <br>
     * field_len = 50 <br>
     * field_width = 50 <br>
     * </code>
     * @param data a sequence of data that the SOM should be trained with
     * @return a SOM over vectorial data
     */
    public SOM makeVectorialSOM(List data) {

        if (params.project_func == null) {
            ProjectionFunc projectf = new GeneralProjectionFunc();

            projectf.setDistanceFunc(new VectorDistanceFunc());
            projectf.setFindBestMatchFunc(new NaiveFindBestMatchFunc());
            projectf.setUpdateFunc(new VectorUpdateFunc());

            params.project_func = (ProjectionFunc) projectf;
        }

        if (params.stop_func == null) {
            params.stop_func = (StopFunc) new VectorStopFunc();
        }

        if (init_func == null) {
            init_func = (InitFunc) new VectorInitFunc();
        }

        if (params.show_status_func == null) {
            params.show_status_func = new SimpleShowStatusFunc();
        }

        return (SOM) makeSOM(data);
    }
}
