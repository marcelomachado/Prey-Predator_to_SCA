package util;

/**
 *
 * @author marcelo
 */
public class LearningStyle {

    public LearningStyle() {
    }

    public int calculateFitnessFunction(int learnerAtvRef, int learningMaterialAtv, int learningMaterialRef) {
        int learningMaterialMaxAtv = 6;
        int learningMaterialMaxRef = 6;
        return -Math.abs(((learningMaterialMaxAtv - learningMaterialAtv) - learnerAtvRef) - ((learningMaterialMaxRef - learningMaterialRef) + learnerAtvRef)) + 12;
    }

    //    sum(-| [(MaterialMaxAtv - Material_i_Atv) -AlunoAtvRef]- [(MaterialMaxRef - Material_i_Ref) + AlunoAtvRef)] + 
    //    [(MaterialMaxSen - Material_i_Sen) -AlunoSenInt]- [(MaterialMaxInt - Material_i_Int) + AlunoSenInt)] +
    //    [(MaterialMaxVis - Material_i_Vis) -AlunoVisVer]- [(MaterialMaxVer - Material_i_Ver) + AlunoVisVer)] +
    //    [(MaterialMaxAtvSeq - Material_i_Seq) -AlunoSeqGlo]- [(MaterialMaxGlo - Material_i_Glo) + AlunoSeqGlo)] |)_i_0_N
    //    _____________________________________________________________________________________________________________________
    //                                               sum(individual[i])_i_0_N
    public int calculateFitnessFunctionTotal(int learnerAtvRef, int learningMaterialAtv, int learningMaterialRef,
            int learnerSenInt, int learningMaterialSen, int learningMaterialInt,
            int learnerVisVer, int learningMaterialVis, int learningMaterialVer,
            int learnerSeqGlo, int learningMaterialSeq, int learningMaterialGlo) {
        int learningMaterialMaxAtv = 6;
        int learningMaterialMaxRef = 6;
        int atvRef;

        int learningMaterialMaxSen = 1;
        int learningMaterialMaxInt = 1;
        int senInt;

        int learningMaterialMaxVis = 1;
        int learningMaterialMaxVer = 1;
        int visVer;

        int learningMaterialMaxSeq = 1;
        int learningMaterialMaxGlo = 1;
        int seqGlo;

        atvRef = ((learningMaterialMaxAtv - learningMaterialAtv) - learnerAtvRef) - ((learningMaterialMaxRef - learningMaterialRef) + learnerAtvRef);
        senInt = ((learningMaterialMaxSen - learningMaterialSen) - learnerSenInt) - ((learningMaterialMaxInt - learningMaterialInt) + learnerSenInt);
        visVer = ((learningMaterialMaxVis - learningMaterialVis) - learnerVisVer) - ((learningMaterialMaxVer - learningMaterialVer) + learnerVisVer);
        seqGlo = ((learningMaterialMaxSeq - learningMaterialSeq) - learnerSeqGlo) - ((learningMaterialMaxGlo - learningMaterialGlo) + learnerSeqGlo);

        return -Math.abs(atvRef) + -Math.abs(senInt) + -Math.abs(visVer) + -Math.abs(seqGlo);
    }

}
